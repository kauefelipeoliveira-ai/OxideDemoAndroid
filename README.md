# Oxide Demo Panel — Android

Projeto Android nativo em Kotlin para um painel demonstrativo com tema azul/preto.

## Recursos
- Controles visuais de ESP (jogadores, esqueleto, itens e veículos)
- Aimbot apenas demonstrativo, sem integração com o jogo
- Ajustes de FOV por controles deslizantes
- Opção demonstrativa de ignorar aliados e verificação de visibilidade
- Seleção de idioma Português/English
- Salvar/carregar preferências com SharedPreferences
- Restaurar padrões

**Importante:** o app não lê nem modifica o Oxide, não injeta código e não fornece cheats funcionais para partidas online.

## Como gerar o APK
1. Abra a pasta `OxideDemoAndroid` no Android Studio (versão recente).
2. Aguarde a sincronização do Gradle e a instalação dos componentes solicitados.
3. Selecione **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. O APK de depuração será gerado em `app/build/outputs/apk/debug/app-debug.apk`.

Requisitos de compilação: Android Studio, JDK 17 e Android SDK Platform 35.


## Build APK online (without installing Android Studio on your phone)
This project includes a GitHub Actions workflow at `.github/workflows/build-apk.yml`.
1. Upload the project folder to a GitHub repository.
2. Open the repository's **Actions** tab.
3. Select **Build Oxide Demo APK** and press **Run workflow** (or push to `main`).
4. When the run finishes, open it and download the `oxide-demo-debug-apk` artifact.
5. Extract the artifact ZIP to get `app-debug.apk`.
