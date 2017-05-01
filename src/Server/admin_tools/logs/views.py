# -*- coding: utf-8 -*-
from __future__ import unicode_literals
import os

# Create your views here.
from django.http import HttpResponse


from django.shortcuts import render_to_response

def index(request):
    log = read_file()
    return render_to_response('logs/index.html', {'log': log})

def read_file():
    f = open( os.path.join(os.environ['PPZ_HOME'], 'log.txt'))
    out = list(f)
    f.close()
    return out
