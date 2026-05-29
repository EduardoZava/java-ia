package com.eduardozava.javaia.agents;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.ApiDesign;
import com.eduardozava.javaia.model.ApiGenerationInput;
import com.eduardozava.javaia.model.ServiceArchitecture;
import com.eduardozava.javaia.skills.ApiArtifactSkill;
import org.springframework.stereotype.Component;

@Component
public class ApiAgent {

    private final ApiArtifactSkill apiArtifactSkill;

    public ApiAgent(ApiArtifactSkill apiArtifactSkill) {
        this.apiArtifactSkill = apiArtifactSkill;
    }

    public ApiDesign generate(AnalyzedSpec spec, ServiceArchitecture architecture) {
        return apiArtifactSkill.execute(new ApiGenerationInput(spec, architecture));
    }
}
