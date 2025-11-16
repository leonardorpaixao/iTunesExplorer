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

O projeto segue uma arquitetura modular limpa com padrão **MVI (Model-View-Intent)**:

```
@iTunesExplorer/
├── composeApp/          # Aplicação principal
├── core/
│   ├── network/         # Camada de rede e API
│   ├── error/           # Tratamento de erros
│   └── common/          # Utilitários comuns + Base MVI
├── design-system/       # Componentes de UI reutilizáveis
└── features/
    └── home/            # Feature Home com tabs (Álbuns, Pesquisa, Preferências)
```

### MVI (Model-View-Intent)

O projeto utiliza o padrão MVI para gerenciamento de estado:

- **ViewState**: Estado imutável da UI (data classes)
- **ViewIntent**: Intenções do usuário (sealed classes)
- **ViewEffect**: Efeitos colaterais únicos (toasts, navegação)
- **MviViewModel**: Base para todos os ViewModels

Veja a documentação completa em [docs/MVI_ARCHITECTURE.md](docs/MVI_ARCHITECTURE.md)

## 🎨 Features

### Tab Álbuns
- **Top Álbuns**: Recomendações dos álbuns mais populares
- **Navegação para Detalhes**: Clique no álbum para ver mais informações

### Tab Pesquisa
- **Busca por Texto**: Campo de pesquisa para encontrar conteúdo específico
- **Filtros por MediaType**: Chips para filtrar por Música, Filme, Podcast, App, etc.
- **Resultados Dinâmicos**: Atualização em tempo real conforme filtros são aplicados

### Tab Preferências
- **Em Desenvolvimento**: Área para configurações futuras

### Geral
- **Interface Responsiva**: UI adaptada para cada plataforma
- **Bottom Navigation**: Navegação intuitiva entre tabs
- **TopBar com Logo**: Clique no nome do app para retornar à tab Álbuns

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

## 🧪 Testes

O projeto possui testes unitários abrangentes para todos os ViewModels usando:

- **Kotlin Test**: Framework de testes padrão
- **Kotlinx Coroutines Test**: Testes de código assíncrono
- **Turbine**: Testes de Flows

### Executar Testes

```bash
# Executar todos os testes do módulo home
./gradlew :features:home:testDebugUnitTest

# Ver relatório HTML
open features/home/build/reports/tests/testDebugUnitTest/index.html
```

### Cobertura

- ✅ **HomeScreenModel**: 4 testes
- ✅ **AlbumsTabModel**: 4 testes
- ✅ **SearchTabModel**: 9 testes
- ✅ **PreferencesTabModel**: 2 testes

**Total**: 19 testes, 100% passando

Veja a documentação completa em [docs/TESTING.md](docs/TESTING.md)

## 📄 Licença

Este projeto é de código aberto para fins educacionais.

## 👨‍💻 Desenvolvimento

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request
