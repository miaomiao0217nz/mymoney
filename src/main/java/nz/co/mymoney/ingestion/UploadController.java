package nz.co.mymoney.ingestion;

import nz.co.mymoney.transaction.TransactionRepository;
import nz.co.mymoney.user.User;
import nz.co.mymoney.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    TransactionParser transactionParser;
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/upload/{type}")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(authentication.getName());

        transactionParser.parseAndImport(user.getId(), file.getInputStream(), file.getOriginalFilename(),
                ts -> transactionRepository.saveAll(ts).size());
        return new ResponseEntity(HttpStatus.OK);
    }


}
