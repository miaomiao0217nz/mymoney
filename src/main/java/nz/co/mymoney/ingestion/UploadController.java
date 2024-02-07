package nz.co.mymoney.ingestion;

import nz.co.mymoney.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    TransactionParser transactionParser;
    @Autowired
    TransactionRepository transactionRepository;

    @PostMapping("/{type}")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        transactionParser.parseAndImport(file.getInputStream(), file.getOriginalFilename(),
                ts -> transactionRepository.saveAll(ts).size());
        return new ResponseEntity(HttpStatus.OK);
    }


}
