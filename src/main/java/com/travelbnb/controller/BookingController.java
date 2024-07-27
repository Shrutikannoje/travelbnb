package com.travelbnb.controller;
import com.travelbnb.entity.AppUser;
import com.travelbnb.entity.Booking;
import com.travelbnb.entity.Property;
import com.travelbnb.repository.BookingRepository;
import com.travelbnb.repository.PropertyRepository;
import com.travelbnb.service.BucketService;
import com.travelbnb.service.PDFService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private PropertyRepository propertyRepository;
    private BookingRepository bookingRepository;

    private PDFService pdfService;

    private BucketService bucketService;

    public BookingController(PropertyRepository propertyRepository, BookingRepository bookingRepository, PDFService pdfService, BucketService bucketService) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.pdfService = pdfService;

        this.bucketService = bucketService;
    }


    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestParam long propertyId,
            @AuthenticationPrincipal AppUser user,
            @RequestBody Booking booking
            ) {

          Property property = propertyRepository.findById(propertyId).get();
          int nightlyPrice = property.getPrice();
          int totalPrice = nightlyPrice*booking.getTotalNights();


         booking.setProperty(property);
         booking.setAppUser(user);
         booking.setPrice(totalPrice);
         Booking savedBooking = bookingRepository.save(booking);
         boolean b = pdfService.generatePDF("D://pdf_example//" + "booking-Confirmation-id"+savedBooking.getId()+".pdf",savedBooking);
           if (b) {
           try{
               MultipartFile file = BookingController.convert("D://pdf_example//" + "booking-Confirmation-id"+savedBooking.getId()+".pdf");
               String uploadFileUrl ="http://confirmation.html";
                       // bucketService.uploadFile(file, "psa14");
               System.out.println(uploadFileUrl);
           }catch(Exception e){
               e.printStackTrace();
           }
           }
           return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);

    }

    //for converting filePath into MultiPartFile
    public static MultipartFile convert(String filePath) throws IOException {
        // Load the file from the specified path
        File file = new File(filePath);

        // Read the file content into array
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // Convert byte array to a ByteArrayResource
        Resource resource = new ByteArrayResource(fileContent);

        // Create MultipartFile from Resource
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                // You can determine and set the content type here
                try {
                    return Files.probeContentType(file.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean isEmpty() {
                return file.length() == 0;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(fileContent);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try (InputStream inputStream = new ByteArrayInputStream(fileContent);
                     OutputStream outputStream = new FileOutputStream(dest)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        };

        return multipartFile;
    }
}
