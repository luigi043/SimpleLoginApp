#!/bin/bash

echo "🔧 Compilando projeto..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Build realizado com sucesso!"
    echo ""
    echo "📱 APK gerado em:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "🚀 Para instalar no dispositivo conectado:"
    echo "   ./gradlew installDebug"
    echo ""
    echo "🧪 Para executar testes:"
    echo "   ./gradlew test"
else
    echo "❌ Erro no build. Verifique os problemas acima."
fi
