#!/bin/bash
set -e

echo "🧪 启动测试环境..."
cd "$(dirname "$0")/../.."
mkdir -p logs-test upload-test

docker-compose -f docker/compose/docker-compose.test.yml up -d --build

echo "✅ 测试环境启动完成: http://localhost:8081"
docker-compose -f docker/compose/docker-compose.test.yml ps