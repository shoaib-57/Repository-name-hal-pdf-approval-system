package com.hal.pdf_approval_system.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QrPdfService {

    public String generateQrCode(String qrText)
            throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(
                qrText,
                BarcodeFormat.QR_CODE,
                250,
                250);

        BufferedImage image = new BufferedImage(
                250,
                250,
                BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 250; x++) {
            for (int y = 0; y < 250; y++) {

                image.setRGB(
                        x,
                        y,
                        bitMatrix.get(x, y)
                                ? 0xFF000000
                                : 0xFFFFFFFF);
            }
        }

        File qrDirectory = new File("uploads/qr");

        if (!qrDirectory.exists()) {
            qrDirectory.mkdirs();
        }

        String qrPath =
                "uploads/qr/"
                        + System.currentTimeMillis()
                        + ".png";

        ImageIO.write(
                image,
                "PNG",
                new File(qrPath));

        return qrPath;
    }

    /*
     * Adds the QR code to the LAST EXISTING PAGE.
     *
     * Important:
     * - Does NOT create a new page.
     * - Does NOT create a signature.
     * - Does NOT draw a footer line.
     * - Only stamps the QR code.
     */
    public void appendQrPage(
            String pdfPath,
            String qrImagePath)
            throws Exception {

        System.out.println("PDF Path: " + pdfPath);
        System.out.println("QR Path: " + qrImagePath);

        try (PDDocument document =
                     Loader.loadPDF(new File(pdfPath))) {

            if (document.getNumberOfPages() == 0) {
                throw new IllegalStateException(
                        "PDF has no pages");
            }

            /*
             * Get the LAST existing page.
             */
            PDPage page =
                    document.getPage(
                            document.getNumberOfPages() - 1);

            PDRectangle mediaBox =
                    page.getMediaBox();

            float pageWidth =
                    mediaBox.getWidth();

            float pageHeight =
                    mediaBox.getHeight();

            /*
             * Adaptive QR size.
             *
             * QR size changes according to the
             * size of the PDF page.
             */
            float qrSize =
                    Math.min(
                            pageWidth * 0.16f,
                            pageHeight * 0.13f);

            /*
             * Keep QR within a practical range.
             */
            qrSize =
                    Math.max(
                            70f,
                            Math.min(qrSize, 110f));

            /*
             * Page margin.
             */
            float sideMargin =
                    Math.max(
                            36f,
                            pageWidth * 0.05f);

            float bottomMargin =
                    Math.max(
                            30f,
                            pageHeight * 0.045f);

            /*
             * QR POSITION
             *
             * Bottom-left of the last page.
             */
            float qrX =
                    sideMargin;

            float qrY =
                    bottomMargin;

            /*
             * Load QR image.
             */
            PDImageXObject qr =
                    PDImageXObject.createFromFile(
                            qrImagePath,
                            document);

            /*
             * APPEND mode:
             *
             * Adds QR to the existing page.
             * It does not replace the page.
             */
            try (PDPageContentStream stream =
                         new PDPageContentStream(
                                 document,
                                 page,
                                 AppendMode.APPEND,
                                 true,
                                 true)) {

                /*
                 * ONLY QR CODE.
                 *
                 * No signature.
                 * No line.
                 * No new page.
                 */
                stream.drawImage(
                        qr,
                        qrX,
                        qrY,
                        qrSize,
                        qrSize);
            }

            /*
             * Save approved PDF.
             *
             * example:
             *
             * test-upload.pdf
             *        ↓
             * test-upload_QR.pdf
             */
            String stampedPdfPath =
                    pdfPath.replace(
                            ".pdf",
                            "_QR.pdf");

            document.save(
                    stampedPdfPath);

            System.out.println(
                    "Approved PDF saved to: "
                            + stampedPdfPath);
        }
    }
}