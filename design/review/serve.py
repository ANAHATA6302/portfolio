# Static server with an SPA fallback, like Netlify's `/* /index.html 200`.
import http.server, os, sys

class H(http.server.SimpleHTTPRequestHandler):
    extensions_map = {**http.server.SimpleHTTPRequestHandler.extensions_map, '.wasm': 'application/wasm', '.mjs': 'text/javascript'}

    def send_head(self):
        if not os.path.exists(self.translate_path(self.path)):
            self.path = '/index.html'
        return super().send_head()

os.chdir(sys.argv[1])
http.server.ThreadingHTTPServer(('', 8080), H).serve_forever()
