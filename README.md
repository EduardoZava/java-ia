# java-ia

MVP em Java para gerar código Java/Spring Boot de microserviços a partir de uma especificação, usando orquestração de múltiplos subagentes, skills reutilizáveis e integração com modelos DeepSeek.

## Motivação

Acelerar o bootstrap de microserviços Spring com uma base orientada a agentes especializados, reduzindo esforço manual e mantendo coerência arquitetural entre artefatos iniciais.

## Arquitetura proposta (MVP)

Pacotes principais:

- `orchestrator`: coordena o fluxo de geração ponta a ponta
- `agents`: subagentes especializados
  - `SpecAnalyzerAgent`
  - `ArchitectureAgent`
  - `ApiAgent`
  - `ReviewerAgent`
- `skills`: abstração `Skill<I, O>` e skills concretas
- `model`: DTOs internos de especificação e resultado
- `infra`: integração externa com DeepSeek
- `generator`: montagem de artefatos iniciais
- `prompt`: construção de prompts usados pelos agentes
- `src/main/resources/templates`: templates de saída

## Fluxo de geração

1. Cliente envia a especificação básica do microserviço (`serviceName`, `description`, entidades, endpoints)
2. `SpecAnalyzerAgent` normaliza e infere capacidades iniciais
3. `ArchitectureAgent` define pacote base e módulos
4. `ApiAgent` gera artefatos de API iniciais e tenta enriquecer resumo via DeepSeek
5. `ReviewerAgent` valida coerência mínima do resultado
6. `GenerationOrchestrator` consolida saída e retorna os artefatos gerados

## Stack sugerida

- Java 17 (mínimo atual do projeto; recomendável evoluir para Java 21 quando o ambiente suportar)
- Spring Boot 3
- Maven
- Spring Web + Validation
- Cliente HTTP Spring (`RestClient`) para DeepSeek
- JUnit 5 / Spring Boot Test

## Estrutura do projeto

```text
src/main/java/com/eduardozava/javaia/
  orchestrator/
  agents/
  skills/
  model/
  infra/deepseek/
  generator/
  prompt/
src/main/resources/
  templates/
src/test/java/com/eduardozava/javaia/
```

## Configuração DeepSeek (variáveis de ambiente)

O cliente DeepSeek está encapsulado em `infra.deepseek.HttpDeepSeekClient`.

Variáveis suportadas:

- `DEEPSEEK_API_KEY` (obrigatória para chamada real)
- `DEEPSEEK_MODEL` (opcional, padrão: `deepseek-chat`)
- `DEEPSEEK_BASE_URL` (opcional, padrão: `https://api.deepseek.com/chat/completions`)

Comportamento sem chave:

- O MVP não quebra o fluxo de geração.
- O `ApiAgent` registra no resumo que o DeepSeek está desabilitado e segue com geração local.

## Como executar localmente

### Pré-requisitos

- Java 17+
- Maven 3.9+

### Build e testes

```bash
mvn clean test
```

### Subir aplicação

```bash
mvn spring-boot:run
```

### Exemplo de uso (endpoint HTTP)

`POST /api/generation`

```bash
curl -X POST http://localhost:8080/api/generation \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "order-service",
    "description": "Microserviço de pedidos",
    "entities": ["Order", "OrderItem"],
    "endpoints": ["POST /orders", "GET /orders/{id}"]
  }'
```

## Testes do MVP

- `GenerationOrchestratorTest`: valida fluxo mínimo entre subagentes e geração de artefatos
- `GenerationControllerTest`: valida endpoint HTTP e contrato de resposta

## Próximos passos

1. Adicionar geração de projeto completo em disco (módulo Spring inicial, `pom.xml`, classes base)
2. Evoluir skills por domínio (entidade, DTO, repository, testes)
3. Adicionar versionamento de templates e prompts
4. Incluir validação semântica mais forte no `ReviewerAgent`
5. Suportar execução assíncrona e rastreamento por job

## Resumo objetivo do que foi implementado neste MVP

- Base Maven/Spring Boot funcional
- Endpoint HTTP para receber especificação
- Orquestrador com sequência mínima de subagentes
- Abstração de `Skill` e skills concretas
- Integração DeepSeek em camada de infraestrutura com tratamento claro para ausência de chave
- Geração inicial de artefatos em memória e saída consolidada
- Testes básicos para orquestrador e endpoint
