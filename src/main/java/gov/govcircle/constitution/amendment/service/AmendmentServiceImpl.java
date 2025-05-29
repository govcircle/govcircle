package gov.govcircle.constitution.amendment.service;

import gov.govcircle.constitution.amendment.repository.AmendmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmendmentServiceImpl {

    private AmendmentRepository opinionRepository;

}
