package io.github.eduardozava.javaia.agents;

import io.github.eduardozava.javaia.model.GenerationContext;
import io.github.eduardozava.javaia.skills.SpringStructureSkill;
import org.springframework.stereotype.Component;

@Component
public class ArchitectureAgent implements Agent {

    private final SpringStructureSkill springStructureSkill;

    public ArchitectureAgent(SpringStructureSkill springStructureSkill) {
        this.springStructureSkill = springStructureSkill;
    }

    @Override
    public void process(GenerationContext context) {
        springStructureSkill.apply(context);
    }
}
