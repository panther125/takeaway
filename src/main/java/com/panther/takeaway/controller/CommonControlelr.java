package com.panther.takeaway.controller;

import com.panther.takeaway.Utils.UUIDUtils;
import com.panther.takeaway.common.R;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RequestMapping("/common")
@RestController
public class CommonControlelr {

    @Value("${file.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String originalFileName = file.getOriginalFilename();//***.jpg
        String fileName = UUIDUtils.UUID()+originalFileName;

        // 判断当前目录是否存在
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath+fileName));
//            byte[] bytes = file.getBytes();
//            FileCopyUtils.copy(bytes, new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response , String name){

        try {
            // 文件输入流
            FileInputStream fis = new FileInputStream(new File(basePath + name));

            // 响应输出流
            ServletOutputStream os = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];

            while ( (len = fis.read(bytes)) != -1){
                os.write(bytes);
                os.flush();
            }

            // 关闭资源
            fis.close();
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
