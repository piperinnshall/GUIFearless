let s:cpo_save=&cpo
set cpo&vim
imap <C-G>S <Plug>ISurround
imap <C-G>s <Plug>Isurround
inoremap <C-W> u
inoremap <C-U> u
nnoremap  <Cmd>cclose
nnoremap  <Cmd>FzfLua live_grep
nnoremap  <Cmd>nohlsearch|diffupdate|normal! 
nnoremap  <Cmd>FzfLua files
nmap  d
nnoremap  <Cmd>nohlsearch
nnoremap  e <Cmd>Ex
nnoremap  y "+y
vnoremap  y "+y
nnoremap  u <Cmd>UndotreeToggle
omap <silent> % <Plug>(MatchitOperationForward)
xmap <silent> % <Plug>(MatchitVisualForward)
nmap <silent> % <Plug>(MatchitNormalForward)
nnoremap & :&&
xnoremap <silent> <expr> @ mode() ==# 'V' ? ':normal! @'.getcharstr().'' : '@'
xnoremap <silent> <expr> Q mode() ==# 'V' ? ':normal! @=reg_recorded()' : 'Q'
xmap S <Plug>VSurround
nnoremap Y y$
omap <silent> [% <Plug>(MatchitOperationMultiBackward)
xmap <silent> [% <Plug>(MatchitVisualMultiBackward)
nmap <silent> [% <Plug>(MatchitNormalMultiBackward)
omap <silent> ]% <Plug>(MatchitOperationMultiForward)
xmap <silent> ]% <Plug>(MatchitVisualMultiForward)
nmap <silent> ]% <Plug>(MatchitNormalMultiForward)
xmap a% <Plug>(MatchitVisualTextObject)
nmap cS <Plug>CSurround
nmap cs <Plug>Csurround
nmap ds <Plug>Dsurround
omap <silent> g% <Plug>(MatchitOperationBackward)
xmap <silent> g% <Plug>(MatchitVisualBackward)
nmap <silent> g% <Plug>(MatchitNormalBackward)
xmap gS <Plug>VgSurround
nnoremap gra <Cmd>FzfLua lsp_code_actions
nmap ySS <Plug>YSsurround
nmap ySs <Plug>YSsurround
nmap yss <Plug>Yssurround
nmap yS <Plug>YSurround
nmap ys <Plug>Ysurround
nnoremap z= <Cmd>FzfLua spell_suggest
xmap <silent> <Plug>(MatchitVisualTextObject) <Plug>(MatchitVisualMultiBackward)o<Plug>(MatchitVisualMultiForward)
onoremap <silent> <Plug>(MatchitOperationMultiForward) :call matchit#MultiMatch("W",  "o")
onoremap <silent> <Plug>(MatchitOperationMultiBackward) :call matchit#MultiMatch("bW", "o")
xnoremap <silent> <Plug>(MatchitVisualMultiForward) :call matchit#MultiMatch("W",  "n")m'gv``
xnoremap <silent> <Plug>(MatchitVisualMultiBackward) :call matchit#MultiMatch("bW", "n")m'gv``
nnoremap <silent> <Plug>(MatchitNormalMultiForward) :call matchit#MultiMatch("W",  "n")
nnoremap <silent> <Plug>(MatchitNormalMultiBackward) :call matchit#MultiMatch("bW", "n")
onoremap <silent> <Plug>(MatchitOperationBackward) :call matchit#Match_wrapper('',0,'o')
onoremap <silent> <Plug>(MatchitOperationForward) :call matchit#Match_wrapper('',1,'o')
xnoremap <silent> <Plug>(MatchitVisualBackward) :call matchit#Match_wrapper('',0,'v')m'gv``
xnoremap <silent> <Plug>(MatchitVisualForward) :call matchit#Match_wrapper('',1,'v'):if col("''") != col("$") | exe ":normal! m'" | endifgv``
nnoremap <silent> <Plug>(MatchitNormalBackward) :call matchit#Match_wrapper('',0,'n')
nnoremap <silent> <Plug>(MatchitNormalForward) :call matchit#Match_wrapper('',1,'n')
nnoremap <silent> <Plug>SurroundRepeat .
nnoremap <C-C> <Cmd>cclose
nnoremap <C-G> <Cmd>FzfLua live_grep
nnoremap <C-P> <Cmd>FzfLua files
nmap <C-W><C-D> d
nnoremap <C-L> <Cmd>nohlsearch|diffupdate|normal! 
imap S <Plug>ISurround
imap s <Plug>Isurround
inoremap  u
inoremap  u
let &cpo=s:cpo_save
unlet s:cpo_save
set autowrite
set background=light
set expandtab
set exrc
set fillchars=eob:\ ,fold:\ ,foldopen:│
set grepformat=%f:%l:%c:%m
set grepprg=rg\ --vimgrep\ -uu\ 
set helplang=en
set ignorecase
set runtimepath=~/.config/nvim,~/.nix-profile/etc/xdg/nvim,/etc/profiles/per-user/piperinnshall/etc/xdg/nvim,/run/current-system/sw/etc/xdg/nvim,/nix/var/nix/profiles/default/etc/xdg/nvim,~/.local/share/nvim/site,~/.local/share/nvim/site/pack/core/opt/vim-surround,~/.local/share/nvim/site/pack/core/opt/undotree,~/.local/share/nvim/site/pack/core/opt/everforest,~/.local/share/nvim/site/pack/core/opt/fzf-lua,~/.local/share/nvim/site/pack/core/opt/nvim-jdtls,~/.nix-profile/share/nvim/site,/etc/profiles/per-user/piperinnshall/share/nvim/site,/run/current-system/sw/share/nvim/site,/nix/var/nix/profiles/default/share/nvim/site,/nix/store/kx58wg8y004b5lxf8gnaq7rmkcdjw8x3-neovim-unwrapped-d79a9dc/share/nvim/runtime,/nix/store/kx58wg8y004b5lxf8gnaq7rmkcdjw8x3-neovim-unwrapped-d79a9dc/share/nvim/runtime/pack/dist/opt/netrw,/nix/store/kx58wg8y004b5lxf8gnaq7rmkcdjw8x3-neovim-unwrapped-d79a9dc/share/nvim/runtime/pack/dist/opt/matchit,/nix/store/kx58wg8y004b5lxf8gnaq7rmkcdjw8x3-neovim-unwrapped-d79a9dc/lib/nvim,/nix/var/nix/profiles/default/share/nvim/site/after,/run/current-system/sw/share/nvim/site/after,/etc/profiles/per-user/piperinnshall/share/nvim/site/after,~/.nix-profile/share/nvim/site/after,~/.local/share/nvim/site/after,/nix/var/nix/profiles/default/etc/xdg/nvim/after,/run/current-system/sw/etc/xdg/nvim/after,/etc/profiles/per-user/piperinnshall/etc/xdg/nvim/after,~/.nix-profile/etc/xdg/nvim/after,~/.config/nvim/after
set scrolloff=8
set secure
set shell=/etc/profiles/per-user/piperinnshall/bin/bash
set shiftwidth=2
set noshowmode
set smartcase
set smartindent
set statusline=\ \ \ %f\ %l:%c\ %m
set suffixes=.bak,~,.o,.h,.info,.swp,.obj,.class
set noswapfile
set tabstop=2
set termguicolors
set undofile
set updatetime=50
set winborder=rounded
set window=61
" vim: set ft=vim :
