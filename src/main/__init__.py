import os

from spotipy.oauth2 import SpotifyClientCredentials
import spotipy
import pprint


client_id = os.getenv('CLIENT_ID')
client_secret = os.environ.get('CLIENT_SECRET')

client_credentials_manager = SpotifyClientCredentials(client_id=client_id, client_secret=client_secret)
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

# 인디 장르 를 필터로 사용해 검색하여 모든 인디 음악을 가져오되, 모두 반복문 내에서 한 번에 50개씩 1000개의 음악을 데려온다
for i in range(0, 100, 50):
    track_results = sp.search(q='genre:indie', limit=50, type='track', market="KR", offset=i)
    for j, track in enumerate(track_results['tracks']['items']):
        pprint.pprint(track['artists'])

