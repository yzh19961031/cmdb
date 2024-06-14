# Maven 命令
MVN = mvn

# NPM 命令
NPM = npm

.PHONY: all
all: build-frontend build-backend deploy-docker clean

# 构建前端
.PHONY: build-frontend
build-frontend:
	cd ../frontend && $(NPM) install && $(NPM) run build && mkdir -p ../src/main/resources/static/ && cp -r ./dist/* ../src/main/resources/static/

# 构建后端
.PHONY: build-backend
build-backend:
	cd ../ && $(MVN) package

# 容器化部署
.PHONY: deploy-docker
deploy-docker:
	docker-compose -f docker-compose.yml up -d --build

# 清理
.PHONY: clean
clean:
	cd ../ && $(MVN) clean && rm -rf frontend/dist && rm -rf src/main/resources/static


.PHONY: all build-frontend build-backend deploy-docker clean
