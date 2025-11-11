# iTunes Explorer

Um aplicativo multiplataforma construído com Kotlin Multiplatform (KMP) e Compose Multiplatform que permite explorar o conteúdo do iTunes Store.

## 🚀 Tecnologias

- **Kotlin Multiplatform (KMP)** - Compartilhamento de código entre plataformas
- **Compose Multiplatform** - UI declarativa para todas as plataformas
- **Voyager** - Navegação multiplataforma
- **Kodein** - Injeção de dependência
- **Ktor Client** - Cliente HTTP multiplataforma
- **Coroutines** - Programação assíncrona

## 📱 Plataformas Suportadas

- ✅ Android
- ✅ iOS
- ✅ Web (WASM)
- ✅ Desktop (JVM)

## 🏗️ Arquitetura

O projeto segue uma arquitetura modular limpa:

```
@iTunesExplorer/
├── composeApp/          # Aplicação principal
├── core/
│   ├── network/         # Camada de rede e API
│   ├── error/           # Tratamento de erros
│   └── common/          # Utilitários comuns
├── design-system/       # Componentes de UI reutilizáveis
└── features/
    ├── listing/         # Tela de listagem
    └── details/         # Tela de detalhes
```

## 🎨 Features

- **Pesquisa de Conteúdo**: Busque por músicas, filmes, podcasts, apps e mais
- **Filtros por Tipo de Mídia**: Filtre resultados por categoria
- **Detalhes do Item**: Veja informações detalhadas sobre cada item
- **Conteúdo Relacionado**: Descubra conteúdo similar
- **Interface Responsiva**: UI adaptada para cada plataforma

## 🔧 Como Executar

### Desktop (JVM)
```bash
./gradlew :composeApp:runDesktop
```

### Web (WASM)
```bash
./gradlew :composeApp:runWasm
```
O aplicativo ficará disponível em http://localhost:8080/

### Android
```bash
# Instalar e executar em dispositivo/emulador conectado
./gradlew :composeApp:runAndroid

# Ou apenas construir o APK
./gradlew :composeApp:assembleDebug
```

### iOS
```bash
# Build para Simulador
./gradlew :composeApp:buildIosSimulator

# Build para Dispositivo
./gradlew :composeApp:buildIosDevice
```
Depois do build, abra o projeto no Xcode:
```bash
open iosApp/iosApp.xcodeproj
```

## 📋 Tasks Gradle Disponíveis

| Task | Descrição |
|------|-----------|
| `runDesktop` | Executa o app Desktop (JVM) |
| `runWasm` | Executa o app WASM no navegador |
| `runAndroid` | Instala e executa o app Android em dispositivo/emulador |
| `buildIosSimulator` | Compila o framework iOS para simulador |
| `buildIosDevice` | Compila o framework iOS para dispositivo |

## 📦 Dependências Principais

- Compose Multiplatform 1.7.0
- Kotlin 2.0.0
- Voyager 1.1.0-beta03
- Kodein 7.22.0
- Ktor 3.0.0
- Kotlinx Serialization 1.7.1
- Kotlinx Coroutines 1.9.0

## 🔑 API

O app utiliza a [iTunes Search API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html) pública da Apple.

## 📄 Licença

Este projeto é de código aberto para fins educacionais.

## 👨‍💻 Desenvolvimento

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request
