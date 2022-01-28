FROM node:alpine3.10

ENV PATH_TO_APP=/usr/src/app \
    PORT=3000 \
    HOST=0.0.0.0 \
    NODE_ENV=development

WORKDIR ${PATH_TO_APP}

RUN apk add --update python2 python3 build-base

COPY package.json yarn.lock ./

RUN yarn install

COPY . ./

RUN yarn build

EXPOSE ${PORT}

CMD yarn dev
