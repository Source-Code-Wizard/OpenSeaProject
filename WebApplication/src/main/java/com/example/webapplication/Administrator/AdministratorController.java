package com.example.webapplication.Administrator;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Auction.AuctionDTO;
import com.example.webapplication.Bid.bidDTO;
import com.example.webapplication.User.AuthUserDto;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRequestDto;

import org.apache.commons.fileupload.FileItem;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "https://localhost:3000",allowCredentials = "true")
public class AdministratorController {
    private final AdminService adminService;

    @Autowired
    FilesStorageService storageService;

    @Autowired// This implements the dependency injection design pattern
    public AdministratorController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getRegRequests")
    public List<UserRequestDto> getRegistrationRequests(){
        return adminService.getRegistrationRequests();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/getRegRequests/a")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthUserDto authUserDto){
        return new ResponseEntity<>(adminService.authenticateUser(authUserDto.getUsername()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/getFullUser")
    public ResponseEntity<?> getFullUser(@RequestBody AuthUserDto authUserDto){
        System.out.println(authUserDto.getUsername());
        return new ResponseEntity<>(adminService.returnFullUser(authUserDto.getUsername()), HttpStatus.OK);
    }

  /*  @GetMapping(path = "/mapJsontoXML", consumes = MediaType.APPLICATION_JSON_VALUE *//*produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}*//*)
    public ResponseEntity<Resource>  mapJsonToXML(@RequestBody final Auction request) throws IOException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Auction.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

            //Print XML String to Console
            File file = new File("employee.xml");
            jaxbMarshaller.marshal(request, file);

          *//*  DiskFileItem fileItem = new DiskFileItem("file", "text/plain", false, file.getName(), (int) file.length() , file.getParentFile());
            fileItem.getOutputStream();
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            storageService.save( multipartFile);*//*

        } catch (JAXBException e) {
            e.printStackTrace();
        }

      *//*  String filename = "employee.xml";
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);*//*
        String filename = "employee.xml";
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }*/
}
