import os
import string

from spotipy.oauth2 import SpotifyClientCredentials
import spotipy
import pprint
import csv

client_id = os.getenv('CLIENT_ID')
client_secret = os.environ.get('CLIENT_SECRET')

client_credentials_manager = SpotifyClientCredentials(client_id=client_id, client_secret=client_secret)
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

# artists_results = sp.search("MAGIC!", limit=50, type='artist', market="KR")
# album_results = sp.search("MAGIC!", limit=1, type='album', market="KR")

found_tracks = []
found_artists = []
found_album = []
link_to_api = []

founds = [found_tracks, found_artists, found_album, link_to_api]


def extract_target(target, found):
    for idx, t in enumerate(target):
        temp = [t]
        found[idx].append(temp)


# 인디 장르 를 필터로 사용해 검색하여 모든 인디 음악을 가져오되, 모두 반복문 내에서 한 번에 50개씩 1000개의 음악을 데려온다
for i in range(0, 1000, 50):
    track_results = sp.search(q='genre:indie', limit=50, type='track', market="KR", offset=i)
    for j, track in enumerate(track_results['tracks']['items']):
        pprint.pprint(track)
        tracks = [track['name'], track['artists'][0]['name'], track['album']['name'], track['href']]
        extract_target(tracks, founds)


# pprint.pprint(found_tracks)

with open('songs_test.csv', 'w', newline='') as songsCsvFile:
    tracksWriter = csv.writer(songsCsvFile, dialect='excel', delimiter=',')
    for i in range(len(found_tracks)):
        try:
            tracksWriter.writerow(found_tracks[i] + found_artists[i] + found_album[i] + link_to_api[i])
        except UnicodeEncodeError:
            print('UnicodeEncodeError Occurred!')
            pass

# pprint.pprint(artists_results)
# pprint.pprint(album_results)
