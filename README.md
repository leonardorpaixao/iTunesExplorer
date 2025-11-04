# iTunes Explorer

Um aplicativo multiplataforma construído com Kotlin Multiplatform (KMP) e Compose Multiplatform que permite explorar o conteúdo do iTunes Store.

## 🚀 Tecnologias

- **Kotlin Multiplatform (KMP)** - Compartilhamento de código entre plataformas
- **Compose Multiplatform** - UI declarativa para todas as plataformas
- **Voyager** - Navegação multiplataforma
- **Koin** - Injeção de dependência
- **Ktorfit** - Cliente HTTP type-safe baseado em Ktor
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

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
```bash
./gradlew :composeApp:iosSimulatorArm64Binaries
```
Abra o projeto iOS no Xcode para executar no simulador.

### Web (WASM)
```bash
./gradlew :composeApp:wasmJsBrowserRun
```

### Desktop
```bash
./gradlew :composeApp:run
```

## 📦 Dependências Principais

- Compose Multiplatform 1.7.0
- Kotlin 2.0.0
- Voyager 1.1.0-beta02
- Koin 4.0.0
- Ktor 3.0.0
- Ktorfit 2.1.0

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
