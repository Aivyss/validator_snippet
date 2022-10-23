# Validator

## 사용기술
- Spring Application Context
- Spring AOP, ControllerAdvice
- Kotlin Reflection, Kotlin Annotation
- 인터페이스 추상화

## 개발 이유 및 목적
현업 프로젝트에서 Spring Data Validation을 사용할 수 없는 문제가 있어 
직접 개발하고 참여한 프로젝트에 최적화된 기능을 제공하려함. 
단 이 프로젝트는 당시에 개발한 것보다 많이 간소화 되어 있어서 사용하려면 수정이 필요함.
Code Snippet으로 보존하려고 작성함.

## 개선점?
많이 간소화해 에러에 대한 정보가 적음. Argument Validation
에러 정보의 추상화를 위해서 어떤 것이 공통적으로 요구되는지 살펴볼 필요가 있고 그것을 이용해 고도화 해야함.