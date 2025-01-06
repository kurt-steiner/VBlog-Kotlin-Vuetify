package com.steiner.vblog

object ArticleConfigure {
    const val TITLE_LENGTH = 256
}

object CategoryConfigure {
    const val NAME_LENGTH = 64
}

object TagConfigure {
    const val NAME_LENGTH = 64
}

object ImageItemConfigure {
    const val NAME_LENGTH = 4096
    const val PATH_LENGTH = 4096
}

object UserConfigure {
    const val NAME_LENGTH = 256
    const val NICKNAME_LENGTH = 256
    const val PASSWORD_LENGTH = 1024
    const val EMAIL_LENGTH = 256
}

const val AuthenticationHeader = "Authentication"