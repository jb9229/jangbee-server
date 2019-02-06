package com.jangbee.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by test on 2018-07-21.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CommonController {
    private static String BUCKET_FIRMIMG_DIR = "asset/img/";
    private final S3Uploader s3Uploader;


    @RequestMapping(value="/common/image", method = RequestMethod.POST )
    public String uploadFirmPhoto(@RequestParam("img")MultipartFile img) throws IOException {


        return s3Uploader.upload(img, BUCKET_FIRMIMG_DIR);
    }

    @RequestMapping(value="/common/image/remove", method = RequestMethod.POST)
    public ResponseEntity removeFirmPhoto(@RequestParam("imgName") String imgName) throws IOException {

        s3Uploader.removeS3(BUCKET_FIRMIMG_DIR+imgName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
