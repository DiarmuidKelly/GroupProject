'use strict';

angular.module('VCA_WebApp.version', [
  'VCA_WebApp.version.interpolate-filter',
  'VCA_WebApp.version.version-directive'
])

.value('version', '0.1');
