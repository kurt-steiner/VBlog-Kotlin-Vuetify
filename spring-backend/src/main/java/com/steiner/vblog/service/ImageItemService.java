package com.steiner.vblog.service;

import com.steiner.vblog.Constants;
import com.steiner.vblog.dto.request.PostImageItemRequest;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.mapper.ImageItemMapper;
import com.steiner.vblog.model.ImageItem;
import com.steiner.vblog.table_metadata.ImageItemsMetadata;
import com.steiner.vblog.util.result.Result;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ImageItemService {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    ImageItemsMetadata metadata;

    @Resource
    ImageItemMapper mapper;

    @Resource(name = "storage")
    String storagePath;

    @Nonnull
    public ImageItem insertOne(@Nonnull MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("untitled");
        String dateString = dateFormat.format(now);

        String filenameEncoded =  "%s.%s".formatted(dateString, filename);
        String absolutePath = Paths.get(storagePath, filenameEncoded).toFile().getAbsolutePath();

        PostImageItemRequest request = new PostImageItemRequest(filenameEncoded, absolutePath);

        File targetFile = new File(absolutePath);
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                throw new ServerInternalException(e);
            }
        }

        // transfer file
        try(InputStream inputStream = file.getInputStream();
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(absolutePath)
            )) {

            // TODO maybe I should create a thread pool
            transferFile(inputStream, outputStream);
            int result = mapper.insertOne(metadata, request);

            if (result < 0) {
                throw new ServerInternalException("insert image item failed");
            }

            Objects.requireNonNull(request.returningId);
            return mapper.findOne(metadata, request.returningId)
                    .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
        } catch (IOException exception) {
            throw new ServerInternalException(exception);
        }
    }

    public Optional<ImageItem> findOne(int id) {
        return mapper.findOne(metadata, id);
    }

    private void transferFile(@Nonnull InputStream from, @Nonnull OutputStream to) throws IOException {
        byte [] buffer = new byte[Constants.BufferSize];

        int resultLength = from.read(buffer, 0, Constants.BufferSize);
        while (resultLength != -1) {
            to.write(buffer, 0, resultLength);
            resultLength = from.read(buffer, 0, Constants.BufferSize);
        }

        to.flush();
    }
}
