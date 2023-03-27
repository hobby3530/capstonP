from bs4 import BeautifulSoup
from urllib.request import urlopen

response = urlopen('https://urlhaus.abuse.ch/browse/')
soup = BeautifulSoup(response, 'html.parser')

for anchor in soup.select('a'):
    print(anchor)
    
# <a class="navbar-brand" href="/">
# <img alt="URLhaus" height="40" src="/images/urlhaus_logo.png" width="150"/>
# </a>
# <a class="nav-link" href="/browse/" title="Browse the URLhaus database">Browse</a>
# <a class="nav-link" href="/api/" title="API">API</a>
# <a class="nav-link" href="/feeds/" title="Feeds">Feeds</a>
# <a class="nav-link" href="/statistics/" title="Statistics">Statistics</a>
# <a class="nav-link" href="/about/" title="About">About</a>
# <a href="https://urlhaus-api.abuse.ch/" target="_parent">https://urlhaus-api.abuse.ch/</a>
    
for anchor in soup.select('a'):
    print(anchor.text)

# Browse
# API
# Feeds
# Statistics
# About
# https://urlhaus-api.abuse.ch/