#!/bin/bash
set -e  # 遇到错误时退出
set -o pipefail  # 管道中的错误也捕获

# 添加调试信息
echo "=== 脚本开始执行 ==="
echo "当前目录: $(pwd)"
echo "脚本路径: $0"

# 记录日志 - 修复版本
LOG_DIR="logs"
LOG_FILE="$LOG_DIR/startup.log"

# 创建日志目录（如果不存在）
mkdir -p "$LOG_DIR"

# 同时输出到控制台和日志文件（追加模式）
exec > >(tee -a "$LOG_FILE") 2>&1

echo "日志文件: $LOG_FILE"
echo "开始时间: $(date '+%Y-%m-%d %H:%M:%S')"

# 检查依赖
echo "=== 检查依赖 ==="
check_dependency() {
    if ! command -v "$1" &> /dev/null; then
        echo "❌ 未找到 $1，请先安装"
        return 1
    fi
    echo "✅ $1 已安装: $(which $1)"
}

check_dependency docker || exit 1
check_dependency docker-compose || exit 1

# 进入项目目录
echo "=== 设置工作目录 ==="
PROJECT_ROOT="/c/project/fuseaitools-server"
if [ ! -d "$PROJECT_ROOT" ]; then
    echo "❌ 项目目录不存在: $PROJECT_ROOT"
    exit 1
fi
cd "$PROJECT_ROOT" || exit 1
echo "当前目录: $(pwd)"

# 创建必要目录
echo "=== 创建目录 ==="
mkdir -p logs upload
echo "✅ 目录创建完成"

# 执行 docker-compose
echo "=== 启动 Docker 服务 ==="
echo "执行命令: docker-compose -f docker/compose/docker-compose.prod.yml up -d --build"

if docker-compose -f docker/compose/docker-compose.prod.yml up -d --build; then
    echo "✅ Docker 服务启动成功"
else
    echo "❌ Docker 服务启动失败"
    echo "查看日志: logs/startup.log"
    exit 1
fi

# 等待服务启动
echo "=== 等待服务启动 ==="
sleep 10

# 检查服务状态
echo "=== 检查服务状态 ==="
docker-compose -f docker/compose/docker-compose.prod.yml ps

# 健康检查
echo "=== 健康检查 ==="
if curl -f http://localhost:8080/actuator/health; then
    echo "✅ 服务健康检查通过"
else
    echo "⚠️  健康检查失败，服务可能仍在启动中"
fi

echo ""
echo "✅ 脚本执行完成"
echo "📝 详细日志: $LOG_FILE"
echo "🚀 应用地址: http://localhost:8080"