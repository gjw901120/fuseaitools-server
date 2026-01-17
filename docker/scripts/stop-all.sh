#!/bin/bash

echo "🛑 停止所有环境..."
docker-compose -f compose/docker-compose.dev.yml down 2>/dev/null || true
docker-compose -f compose/docker-compose.test.yml down 2>/dev/null || true
docker-compose -f compose/docker-compose.prod.yml down 2>/dev/null || true

echo "🧹 清理资源..."
docker system prune -f

echo "✅ 所有环境已停止"