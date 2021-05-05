package top.zbsong.community.controller;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import top.zbsong.community.dto.FileDTO;
import top.zbsong.community.exception.CustomizeErrorCode;
import top.zbsong.community.exception.CustomizeException;
import top.zbsong.community.provider.COSProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private COSProvider cosProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile mFile = multipartRequest.getFile("editormd-image-file");


        // MutipartFile 转 File，再去调用 upload
        File file = null;
        String resultUrl = "";
        try {
            // 创建file临时文件
            String fileName = mFile.getOriginalFilename();
            int split = fileName.lastIndexOf('.');
            file = File.createTempFile(fileName.substring(0, split), fileName.substring(split));

            // 将CommonsMultipartFile的临时文件的数据转到File 对象的临时文件
            mFile.transferTo(file);

            resultUrl = cosProvider.upload(file);

            file.deleteOnExit();
        } catch (Exception e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(resultUrl);
        return fileDTO;
    }
}
