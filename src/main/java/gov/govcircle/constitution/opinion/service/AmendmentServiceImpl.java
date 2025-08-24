package gov.govcircle.constitution.opinion.service;

import gov.govcircle.constitution.opinion.repository.AmendmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmendmentServiceImpl {

    private AmendmentRepository opinionRepository;

}
