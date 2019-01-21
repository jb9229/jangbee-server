package com.jangbee.common;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by test on 2018-07-21.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CommonController {
    private final S3Uploader s3Uploader;


    @RequestMapping(value="/common/image/upload", method = RequestMethod.POST )
    public String uploadProfilePhoto(@RequestParam("img")MultipartFile img) throws IOException {


        return s3Uploader.upload(img, "asset/img");
    }
}
