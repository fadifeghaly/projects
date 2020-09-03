import tweepy

consumer_key = "not available"
consumer_secret = "not available"
access_token = "not available"
access_token_secret = "not available"
message = """Voici la liste des nouveaux contrevenants : """


# Authentication
auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

# API object
api = tweepy.API(auth)

def post_to_twitter(liste_contrevenants):
    api.update_status(status=message + liste_contrevenants)
