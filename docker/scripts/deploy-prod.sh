#!/bin/bash
set -e

echo "🚀 启动生产环境..."
cd "$(dirname "$0")"

docker-compose -f ../compose/docker-compose.prod.yml up -d --build

echo "✅ 生产环境启动完成"
docker-compose -f ../compose/docker-compose.prod.yml ps