package org.raflab.vodiciservice.converter;

import org.raflab.vodiciservice.dto.request.CertificationsRequest;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.response.CertificationsResponse;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.model.Certifications;
import org.raflab.vodiciservice.model.Guides;

import java.util.ArrayList;
import java.util.List;

public class CertificationsConverter {
    public static Certifications toCertifications(CertificationsRequest certificationsRequest) {
        Certifications certifications = new Certifications();
        certifications.setName(certificationsRequest.getName());
        certifications.setExpiry_date(certificationsRequest.getExpiry_date());
        certifications.setIssuing_body(certificationsRequest.getIssuing_body());
        certifications.setCertificationLevel(certificationsRequest.getCertificationLevel());

        return certifications;
    }
    public static CertificationsResponse toCertificationResponse(Certifications certifications) {
        CertificationsResponse certificationsResponse = new CertificationsResponse();
        certificationsResponse.setId(certifications.getId());
        certificationsResponse.setName(certifications.getName());
        certificationsResponse.setExpiry_date(certifications.getExpiry_date());
        certificationsResponse.setIssuing_body(certifications.getIssuing_body());
        certificationsResponse.setCertificationLevel(certifications.getCertificationLevel());
        certificationsResponse.setGuideResponse(GuideConverter.toGuidesResponse(certifications.getGuides()));

        return certificationsResponse;
    }

    public static List<CertificationsResponse> toCertificationsResponseList(List<Certifications> certificationsList){
        List<CertificationsResponse> certificationsResponseList = new ArrayList<>();
        for(Certifications cf:certificationsList){
            certificationsResponseList.add(toCertificationResponse(cf));
        }
        return certificationsResponseList;
    }
}
