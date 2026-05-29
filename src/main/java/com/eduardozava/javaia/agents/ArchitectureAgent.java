package com.eduardozava.javaia.agents;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.ServiceArchitecture;
import com.eduardozava.javaia.skills.ArchitectureDraftSkill;
import org.springframework.stereotype.Component;

@Component
public class ArchitectureAgent {

    private final ArchitectureDraftSkill architectureDraftSkill;

    public ArchitectureAgent(ArchitectureDraftSkill architectureDraftSkill) {
        this.architectureDraftSkill = architectureDraftSkill;
    }

    public ServiceArchitecture design(AnalyzedSpec spec) {
        return architectureDraftSkill.execute(spec);
    }
}
