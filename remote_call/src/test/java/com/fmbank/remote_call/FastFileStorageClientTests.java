package com.fmbank.remote_call;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.domain.upload.ThumbImage;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemoteCallApplication.class)
public class FastFileStorageClientTests {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Test
    public void testUploadText() throws IOException {
        String text="asasasas";
        InputStream   inputStream   =   new ByteArrayInputStream(text.getBytes());
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, text.getBytes().length, "asas.text", null);
        System.out.println(storePath);

    }
    @Test
    public void getUploadText() throws IOException {
        DownloadByteArray callback = new DownloadByteArray();
        String group1 = fastFileStorageClient.downloadFile("group1", "M00/00/00/wKiGCGHUXXOADbrkAAAACLn-zxU.asas.t", new DownloadCallback<String>() {
            @Override
            public String recv(InputStream inputStream) throws IOException {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    byte[] b = new byte[1024];
                    int n;
                    while ((n = inputStream.read(b)) != -1) {
                        outputStream.write(b, 0, n);
                    }
                } catch (Exception e) {
                    try {
                        inputStream.close();
                        outputStream.close();
                    } catch (Exception e1) {
                    }
                }
                return outputStream.toString();
            }
        });
        System.out.println("reeeee-"+group1);

    }
    @Test
    public void testUploadImage() throws IOException {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\jc336\\Desktop\\2022-01-04_213844.jpg");
        String path = uploadFile(inputStream, "2022-01-04_213844.jpg");
        System.out.println(path);
    }

    @Test
    public void testDeleteImage() throws IOException {
        fastFileStorageClient.deleteFile("group1/M00/00/00/wKiGCGHUTieAYj-FAACnGwMOE7c013.jpg");
    }

    //    文件上传的封装
    private String uploadFile(InputStream inputStream, String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(suffix);
        try {
            FastImageFile fif = new FastImageFile(inputStream,
                    inputStream.available(), suffix,
                    new HashSet<>(), new ThumbImage(150, 150));
            StorePath storePath = fastFileStorageClient.uploadImage(fif);
            return storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
