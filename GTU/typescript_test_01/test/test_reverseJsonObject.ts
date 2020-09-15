 import { expect } from 'chai';
 import reverseJsonObject from '../src/reverseJsonObject';
  
  describe('test_reverseJsonObject', function() {
    it('testMe', function() {
        let inputValue = {
            hired: {
              be: {
                to: {
                  deserve: 'I'
                }
              }
            }
          };
        let result = reverseJsonObject.testMe(inputValue);
        let outputValue = {
            I: {
            deserve: {
                to: {
                be: 'hired'
                }
            }
            }
        };
        expect(result).equal(outputValue);
    });
  });