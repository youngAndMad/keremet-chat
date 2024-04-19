type config = {
    auth: {
        session: {
            ttl: number // duration in minutes
        }
    },
    redis: {
        host: string,
        port: number,
        password: string
    },
    db: {
        url: string
        username: string
        password: string
    },
    oauth2: {}
}
