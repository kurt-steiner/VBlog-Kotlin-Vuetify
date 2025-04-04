package com.steiner.vblog.controller;

import com.steiner.vblog.Constants;
import com.steiner.vblog.exception.NotFoundException;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.model.ImageItem;
import com.steiner.vblog.service.ImageItemService;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@RequestMapping("/image")
public class ImageItemController {
    @Resource
    ImageItemService service;

    @PostMapping("/upload")
    public Response.Ok<ImageItem> uploadImage(@RequestParam("file")MultipartFile image) {
        ImageItem imageItem = service.insertOne(image);
        return new Response.Ok<>("upload success", imageItem);
    }

    @GetMapping("/download/{id}")
    public void downloadImage(@PathVariable(value = "id", required = true) int id, HttpServletResponse response) {
        Optional<ImageItem> imageItem =  service.findOne(id);
        Consumer<ImageItem> consumer = (image) -> {
            File file = new File(image.path);
            if (!file.exists()) {
                throw new ServerInternalException("file not exist on server file");
            }

            response.reset();
            byte [] buffer = new byte[Constants.BufferSize];
            try(FileInputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                OutputStream outputStream = response.getOutputStream()) {

                response.setContentLengthLong(file.length());

                int result = bufferedInputStream.read(buffer);
                while (result != -1) {
                    outputStream.write(buffer, 0, result);
                    result = bufferedInputStream.read(buffer);
                }

                outputStream.flush();
            } catch (IOException exception) {
                throw new ServerInternalException(exception);
            }

        };

        Runnable action = () -> {
            throw new NotFoundException("no such image item");
        };

        imageItem.ifPresentOrElse(consumer, action);
    }
}
