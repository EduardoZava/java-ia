# java-ia

MVP em Java para geração de código Spring Boot de microserviços a partir de uma especificação, usando múltiplos subagentes, skills reutilizáveis e integração com DeepSeek.

## Motivação

A proposta é acelerar a criação de bases de microserviços Spring com uma arquitetura orientada a agentes, reduzindo esforço manual e padronizando a saída inicial dos projetos.

## Arquitetura de subagentes

Pacotes principais:

- `orchestrator`: coordena o pipeline de geração
- `agents`: subagentes especializados (`SpecAnalyzerAgent`, `ArchitectureAgent`, `ApiAgent`, `ReviewerAgent`)
- `skills`: habilidades reutilizáveis (`Skill`, `SpecSummarySkill`, `SpringStructureSkill`, `ApiSkeletonSkill`)
- `model`: DTOs e modelos internos de entrada/saída
- `infra`: integração externa com DeepSeek (`HttpDeepSeekClient`)
- `generator`: consolidação final do resultado (`GenerationResultAssembler`)
- `prompt`: templates de prompt (`PromptTemplateService`)
- `src/main/resources/templates`: templates de referência de saída

## Fluxo de geração

1. `SpecAnalyzerAgent` interpreta a especificação e gera resumo (DeepSeek/fallback)
2. `ArchitectureAgent` define estrutura inicial do projeto Spring
3. `ApiAgent` produz artefatos iniciais de API
4. `ReviewerAgent` revisa consistência e adiciona warnings
5. `GenerationOrchestrator` consolida o resultado final

## Stack sugerida

- Java 17 (mínimo e padrão do `pom.xml` atual) / Java 21 recomendado para evolução
- Spring Boot 3.3.x
- Maven
- Spring Web + Validation
- JUnit 5 (via `spring-boot-starter-test`)

## Configuração DeepSeek

Variáveis suportadas:

- `DEEPSEEK_API_KEY` (obrigatória para chamadas reais)
- `DEEPSEEK_BASE_URL` (opcional, default: `https://api.deepseek.com`)
- `DEEPSEEK_MODEL` (opcional, default: `deepseek-chat`)

Sem `DEEPSEEK_API_KEY`, o sistema segue com fallback local e retorna warning explícito.

## Como executar localmente

### Build e testes

```bash
mvn test
```

### Subir aplicação

```bash
mvn spring-boot:run
```

### Exemplo de requisição

```bash
curl -X POST http://localhost:8080/api/generation \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "order-service",
    "description": "CRUD de pedidos",
    "endpoints": [
      {"method": "GET", "path": "/orders", "description": "Listar pedidos"},
      {"method": "POST", "path": "/orders", "description": "Criar pedido"}
    ]
  }'
```

## Próximos passos

- Gerar projeto Spring completo em disco (multi-arquivo pronto para compilação)
- Adicionar subagentes de domínio, persistência e testes
- Evoluir templates para OpenAPI, DTOs e camadas de negócio
- Adicionar testes de integração HTTP do endpoint de geração
