'use strict';

describe('VCA_WebApp.version module', function() {
  beforeEach(module('VCA_WebApp.version'));

  describe('version service', function() {
    it('should return current version', inject(function(version) {
      expect(version).toEqual('0.1');
    }));
  });
});
