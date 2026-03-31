package com.rex.SecureQRCodeGenerator.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QrCodeService {

    public String generateQrCode(String text, String fileName) throws Exception {
        int width = 300;
        int height = 300;

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        Path folder = Paths.get("src/main/resources/static/qrcodes");
        Files.createDirectories(folder);

        String filePath = "src/main/resources/static/qrcodes/" + fileName + ".png";
        ImageIO.write(image, "PNG", new File(filePath));

        return "/qrcodes/" + fileName + ".png";
    }
}