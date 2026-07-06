package org.raflab.vodiciservice.service;

import org.raflab.vodiciservice.converter.CertificationsConverter;
import org.raflab.vodiciservice.converter.GuideConverter;
import org.raflab.vodiciservice.dto.request.CertificationsRequest;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.response.CertificationsResponse;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.model.Certifications;
import org.raflab.vodiciservice.model.Guides;
import org.raflab.vodiciservice.repositories.CertificationsRepository;
import org.raflab.vodiciservice.repositories.GuidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificationsService {
    @Autowired
    private CertificationsRepository certificationsRepository;
    @Autowired
    private GuidesRepository guidesRepository;
    public void save(CertificationsRequest  certificationsRequest) {
        Certifications certifications=CertificationsConverter.toCertifications(certificationsRequest);
        Guides guides=guidesRepository.getByFirstNameAndLastName(certificationsRequest.getGuidesRequest().getFirst_name(),certificationsRequest.getGuidesRequest().getLast_name());


        if(guides!=null){
            certifications.setGuides(guides);
        }else {
            Guides newGuides=GuideConverter.toGuides(certificationsRequest.getGuidesRequest());
            guidesRepository.save(newGuides);
            certifications.setGuides(newGuides);
        }

        validateCertificationData(certifications);

        certificationsRepository.save(certifications);
    }
    public List<CertificationsResponse> getAllCertifications() {
        return CertificationsConverter.toCertificationsResponseList(certificationsRepository.findAll());
    }

    public void updateById(CertificationsRequest certificationsRequest,Long id){

        if(certificationsRepository.existsById(id)){
            Certifications certifications=certificationsRepository.findById(id).get();
            certifications.setName(certificationsRequest.getName());
            certifications.setIssuing_body(certificationsRequest.getIssuing_body());
            certifications.setExpiry_date(certificationsRequest.getExpiry_date());
            certifications.setCertificationLevel( certificationsRequest.getCertificationLevel());
            certifications.setGuides(GuideConverter.toGuides(certificationsRequest.getGuidesRequest()));
            certificationsRepository.save(certifications);
        }else {
            certificationsRepository.save(CertificationsConverter.toCertifications(certificationsRequest));
        }
    }


    public CertificationsResponse findbyid(Long id){
        return CertificationsConverter.toCertificationResponse(certificationsRepository.findById(id).get());
    }

    public void deleteById(Long id){
        certificationsRepository.deleteById(id);
    }

    public List<CertificationsResponse> getCertificationByImeAndPrezime(String ime, String prezime){
        return CertificationsConverter.toCertificationsResponseList(certificationsRepository.findCertificationsByImeAndPrezime(ime,prezime));
    }

    public void validateCertificationData(Certifications certifications){
        if(certifications.getName()==null || certifications.getName()==""){
            throw new RuntimeException("Polje name u certification nije uneseno.");
        }
        if(certifications.getExpiry_date()==null){
            throw  new RuntimeException("Polje expiry date u certification nije uneseno.");
        }
        if(certifications.getIssuing_body()==null){
            throw new RuntimeException("Polje issuing body u cerification nije uneseno.");
        }
        if(certifications.getCertificationLevel()==null){
            throw new RuntimeException("Polje certification level u certification nije uneseno.");
        }
        if(certifications.getGuides()==null){
            throw new RuntimeException("Polje guideReqeuest u certification nije uneseno");
        }


    }
}
