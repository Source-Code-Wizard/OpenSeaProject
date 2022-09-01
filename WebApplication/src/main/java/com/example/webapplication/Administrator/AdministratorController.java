package com.example.webapplication.Administrator;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Auction.AuctionDTO;
import com.example.webapplication.Bid.bidDTO;
import com.example.webapplication.User.AuthUserDto;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.json.*;
import org.json.XML;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "https://localhost:3000",allowCredentials = "true")
public class AdministratorController {
    private final AdminService adminService;

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

    @PostMapping(path = "/mapJsontoXML", consumes = MediaType.APPLICATION_JSON_VALUE /*produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}*/)
    public String mapJsonToXML(@RequestBody final Auction request) throws IOException {
        JSONObject jsonObject = new JSONObject(request);
        System.out.println(jsonObject.toString());
        /*String str="{\"buyPrice\":450,\"itemId\":1,\"currently\":2100,\"firstBid\":500,\"numOfBids\":5,\"name\":\"Xbox\",\"description\":\"brandnewbeautifulhandmadeeuropeanblownglassornamentfromchristopherradko.thisparticularornamentfeaturesasnowmanpairedwithalittlegirlbundledupinherepalebluecoatsleddingalongonasilverandbluesledfilledwithpackages.theornamentisapproximately5_talland4_wide.brandnewandneverdisplayed\",\"location\":\"California\",\"auctionStartedTime\":\"2020-03-01T18:10:40\",\"categories\":[{\"caterogoryName\":\"Gaming\",\"categoryId\":4}],\"auctionEndTime\":\"2021-12-01T18:10:40\",\"bidList\":[{\"localBidDateTime\":\"2022-09-01T22:02:41\",\"bidder\":{\"bidderAddress\":\"greece\",\"bidderCountry\":\"nk\",\"rating\":0,\"bidsList\":[],\"id\":3},\"bid_id\":2,\"bidderUsername\":\"alex\",\"moneyAmount\":1000.04},{\"localBidDateTime\":\"2022-08-28T15:58:27\",\"bidder\":{\"bidderAddress\":\"greece\",\"bidderCountry\":\"ns\",\"rating\":0,\"bidsList\":[],\"id\":4},\"bid_id\":3,\"bidderUsername\":\"mous\",\"moneyAmount\":50},{\"localBidDateTime\":\"2022-09-01T21:55:33\",\"bidder\":{\"rating\":0,\"bidsList\":[],\"id\":0},\"bid_id\":4,\"bidderUsername\":\"admin\",\"moneyAmount\":44.55},{\"localBidDateTime\":\"2022-08-29T16:49:55\",\"bidder\":{\"bidderAddress\":\"Greece\",\"bidderCountry\":\"Nikea,32\",\"rating\":0,\"bidsList\":[],\"id\":13},\"bid_id\":14,\"bidderUsername\":\"luoa3\",\"moneyAmount\":2100},{\"localBidDateTime\":\"2022-08-29T16:57:33\",\"bidder\":{\"bidderAddress\":\"greece\",\"bidderCountry\":\"kkk\",\"rating\":0,\"bidsList\":[],\"id\":16},\"bid_id\":17,\"bidderUsername\":\"kaulas\",\"moneyAmount\":1600}]}";
        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode node = jsonMapper.readValue(str, JsonNode.class);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
        ObjectWriter ow = xmlMapper.writer().withRootName("root");
        StringWriter w = new StringWriter();
        ow.writeValue(w, node);
        System.out.println(w.toString());*/

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Auction.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

            //Print XML String to Console
            jaxbMarshaller.marshal(request, new File("employee.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }


        String xml = XML.toString(jsonObject);
        return xml;
    }
}
