<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Mail message</title>
</head>
<body>
<p>Hello ${receiverEmail},</p>
<p>Please click the following link to verify your email address:</p>
<p><a href="${link}">${link}</a></p>
<p>This link will expire in ${verificationTokenTtl}</p>
<p>Thank you!</p>
</body>
</html>