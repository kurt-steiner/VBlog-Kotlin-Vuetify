package com.steiner.vblog.plugin

import com.steiner.vblog.service.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    val config = environment.config

    install(Koin) {
        slf4jLogger()
        modules(module {
            single {
                Database.connect(
                    url = config.property("postgres.url").getString(),
                    user = config.property("postgres.user").getString(),
                    password = config.property("postgres.password").getString(),
                    driver = config.property("postgres.driver").getString()
                )
            }

            single {
                ArticleService(get())
            }

            single {
                CategoryService(get())
            }

            single {
                ImageItemService(get())
            }

            single {
                TagService(get())
            }

            single {
                UserService(get())
            }

            single<String>(named("jwt.audience")) {
                config.property("jwt.audience").getString()
            }

            single<String>(named("jwt.domain")) {
                config.property("jwt.domain").getString()
            }

            single<String>(named("jwt.realm")) {
                config.property("jwt.realm").getString()
            }

            single<String>(named("jwt.secret")) {
                config.property("jwt.secret").getString()
            }

            single<Int>(named("summary.max-length")) {
                config.property("custom.summary.max-length").getString().toInt()
            }

            single<String>(named("storage")) {
                val osname = System.getProperty("os.name").lowercase()

                if (osname.contains("win")) {
                    config.property("custom.windows-storage").getString()
                } else if (osname.contains("nix") || osname.contains("nux") || osname.contains("linux")) {
                    config.property("custom.linux-storage").getString()
                } else {
                    throw Exception("running on a unknown os")
                }

            }

            single(named("json")) {
                Json {
                    ignoreUnknownKeys = true
                    classDiscriminator = "type"
                }
            }
        })
    }
}
