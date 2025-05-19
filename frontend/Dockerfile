FROM node:alpine3.10 AS base

ENV PATH_TO_APP=/usr/src/app \
    PORT=3000 \
    HOST=0.0.0.0

WORKDIR ${PATH_TO_APP}

EXPOSE ${PORT}

RUN apk add --update python2 python3 build-base

COPY package.json yarn.lock ./






FROM base AS development

ENV NODE_ENV=development

RUN yarn install

COPY . ./

RUN yarn build

CMD yarn dev






FROM base AS production

ENV NODE_ENV=production

RUN yarn install

COPY . ./

RUN yarn build

CMD yarn start
