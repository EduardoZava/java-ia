package io.github.eduardozava.javaia.agents;

import io.github.eduardozava.javaia.model.GenerationContext;
import io.github.eduardozava.javaia.skills.ApiSkeletonSkill;
import org.springframework.stereotype.Component;

@Component
public class ApiAgent implements Agent {

    private final ApiSkeletonSkill apiSkeletonSkill;

    public ApiAgent(ApiSkeletonSkill apiSkeletonSkill) {
        this.apiSkeletonSkill = apiSkeletonSkill;
    }

    @Override
    public void process(GenerationContext context) {
        apiSkeletonSkill.apply(context);
    }
}
