package org.flow.qr_code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.apache.commons.lang.RandomStringUtils;
import org.flow.models.Ordering;
import org.flow.repositories.OrderingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;


@Component
public class QRCGenerator {
    @Autowired
    private OrderingRepository orderingRepository;

    public String generateQRCode(Long id)
            throws WriterException, IOException {
                System.out.println(id);
        int size = 600;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        String rndchars = RandomStringUtils.randomAlphanumeric(4);
        String uniquefilename = new SimpleDateFormat("'QR_'yyyyMMdd-HHmm'_'").format(new Date());
        Path path = FileSystems.getDefault().getPath("frontend/public/", uniquefilename + rndchars + ".png");
        BitMatrix bitMatrix = qrCodeWriter.encode(uniquefilename + rndchars + ".png", BarcodeFormat.QR_CODE, size, size, hints);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return uniquefilename + rndchars + ".png";
    }
}

