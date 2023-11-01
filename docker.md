# Docker 실행 방법

1. docker 설치 후 실행
2. server 폴더에서 다음 명령어 실행

> ```bash
> # docker-compose 실행(redis + spring)
> docker-compose up -d --build
>
> # docker 컨테이너 확인
> docker ps
>
> # docker 컨테이너 로그 확인
> docker container logs oDuckio-spring
>
> # docker 컨테이너 터미널
> docker exec -it <container id> /bin/bash
>
> # docker-compose 종료 및 컨테이너 삭제
> docker-compose down
> ```

# Dockerfile 빌드 및 푸시 방법

> ```bash
> # 도커 빌드
> # docker build -f {dockerfile} -t {dockerHubName}/{imageName}:{tag} {path}
>
> docker build -f dockerfile-dev -t fabeejoo/oduckio-spring .
>
> # 도커 컨테이터 실행
> # docker run -p {hostPort}:{containerPort} {dockerHubName}/{imageName}:{tag}
>
> docker run -d -p 8000:8000 fabeejoo/oduckio-spring
>
> # 도커 푸시
> # docker push {dockerHubName}/{imageName}:{tag}
>
> docker push fabeejoo/oduckio-spring
>
> # 다양한 아키텍처로 빌드
> # docker buildx build --platform {platform} -f {path} -t {tag} .
>
> docker buildx build --platform linux/amd64 -f ./dockerfile-dev -t fabeejoo/oduckio-spring .
>
> docker buildx build --platform linux/amd64 -f ./dockerfile-prod -t fabeejoo/oduckio-spring:x.x.x .
> ```

# Promethues

> ```bash
> # prometheus 실행
> docker run \
> ```

    -d \
    -p 9090:9090 \
    -v ./prometheus.yml:/etc/prometheus/prometheus.yml \
    prom/prometheus

> ```
>
> ```
