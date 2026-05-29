package com.eduardozava.javaia.skills;

import com.eduardozava.javaia.infra.deepseek.DeepSeekClient;
import com.eduardozava.javaia.infra.deepseek.DeepSeekConfigurationException;
import com.eduardozava.javaia.generator.NamingUtils;
import com.eduardozava.javaia.model.ApiDesign;
import com.eduardozava.javaia.model.ApiGenerationInput;
import com.eduardozava.javaia.model.GeneratedArtifact;
import com.eduardozava.javaia.prompt.PromptTemplateService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiArtifactSkill implements Skill<ApiGenerationInput, ApiDesign> {

    private final DeepSeekClient deepSeekClient;
    private final PromptTemplateService promptTemplateService;

    public ApiArtifactSkill(DeepSeekClient deepSeekClient, PromptTemplateService promptTemplateService) {
        this.deepSeekClient = deepSeekClient;
        this.promptTemplateService = promptTemplateService;
    }

    @Override
    public ApiDesign execute(ApiGenerationInput input) {
        String className = NamingUtils.toPascalCase(input.analyzedSpec().serviceName()) + "Controller";
        String packageName = input.architecture().basePackage() + ".controller";

        String aiComment;
        try {
            aiComment = deepSeekClient.generateText(promptTemplateService.buildApiPrompt(input.analyzedSpec(), input.architecture()));
        } catch (DeepSeekConfigurationException ex) {
            aiComment = "DeepSeek desabilitado: " + ex.getMessage();
        }

        String path = "src/main/java/" + packageName.replace('.', '/') + "/" + className + ".java";
        String content = "package " + packageName + ";\n\n" +
                "import org.springframework.web.bind.annotation.GetMapping;\n" +
                "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/" + input.analyzedSpec().serviceName().toLowerCase() + "\")\n" +
                "public class " + className + " {\n\n" +
                "    @GetMapping(\"/health\")\n" +
                "    public String health() {\n" +
                "        return \"ok\";\n" +
                "    }\n" +
                "}\n";

        return new ApiDesign(
                List.of(new GeneratedArtifact(path, content)),
                aiComment
        );
    }

}
