type config = {
    email: {
        smtp: {
            host: string,
            port: number,
            secure: boolean,
            auth: {
                user: string,
                pass: string
            }
        }
    },
    auth: {
        emailVerificationEnabled: boolean, // default true
        jwt: {
            secret: string,
            expiresIn: number // duration in minutes
        },
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
