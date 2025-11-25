// 現在のページに合わせてナビゲーションをハイライトするシンプルなスクリプト
(function() {
  var path = window.location.pathname.split("/").pop();
  if (!path) {
    path = "index.html";
  }
  var links = document.querySelectorAll('header nav a');
  for (var i = 0; i < links.length; i++) {
    var href = links[i].getAttribute('href');
    if (href === path) {
      links[i].classList.add('active');
    }
  }
})();
