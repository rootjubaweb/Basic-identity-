import test from 'node:test';
import assert from 'node:assert/strict';
import {
  extractEnterpriseToken,
  validateProjectId,
  buildPolicyBody,
  generateColabStep1Code,
  generateColabStep2Code,
  generateAndroidDpcQrPayload,
} from './tokenParser';
import { AndroidPolicy } from '../types/android';

const mockDefaultPolicy: AndroidPolicy = {
  playStoreMode: 'BLACKLIST',
  passwordScope: 'SCOPE_PROFILE',
  cameraDisabled: false,
  screenCaptureDisabled: false,
  crossProfileCopyPasteAllowed: true,
  installUnknownSourcesAllowed: false,
  modifyAccountsDisabled: false,
  factoryResetDisabled: false,
  autoTimeRequired: false,
};

test('extractEnterpriseToken extracts token from standard localhost URL query', () => {
  const url = 'https://localhost/?enterpriseToken=ya29.a0AfH6SM_test_token_xyz123';
  const result = extractEnterpriseToken(url);
  assert.equal(result.success, true);
  assert.equal(result.token, 'ya29.a0AfH6SM_test_token_xyz123');
});

test('extractEnterpriseToken handles extra query parameters and encoding', () => {
  const url = 'https://localhost:8000/callback?state=xyz&enterpriseToken=token%2Bspecial_456&scope=android';
  const result = extractEnterpriseToken(url);
  assert.equal(result.success, true);
  assert.equal(result.token, 'token+special_456');
});

test('extractEnterpriseToken handles hash fragment', () => {
  const url = 'https://localhost/#enterpriseToken=fragment_token_789';
  const result = extractEnterpriseToken(url);
  assert.equal(result.success, true);
  assert.equal(result.token, 'fragment_token_789');
});

test('extractEnterpriseToken accepts raw token string', () => {
  const raw = 'ya29.a0AfH6SM9876543210tokenabc';
  const result = extractEnterpriseToken(raw);
  assert.equal(result.success, true);
  assert.equal(result.token, 'ya29.a0AfH6SM9876543210tokenabc');
});

test('extractEnterpriseToken returns error for invalid or empty input', () => {
  assert.equal(extractEnterpriseToken('').success, false);
  assert.equal(extractEnterpriseToken('   ').success, false);
  assert.equal(extractEnterpriseToken('https://localhost/something_without_token').success, false);
});

test('validateProjectId checks valid and invalid GCP project IDs', () => {
  assert.equal(validateProjectId('my-android-enterprise-123').valid, true);
  assert.equal(validateProjectId('short').valid, false); // < 6 chars
  assert.equal(validateProjectId('123-starts-with-number').valid, false);
  assert.equal(validateProjectId('Uppercase-Not-Allowed').valid, false);
});

test('buildPolicyBody creates correct Android Management API payload', () => {
  const policy: AndroidPolicy = {
    ...mockDefaultPolicy,
    cameraDisabled: true,
    installUnknownSourcesAllowed: true,
  };
  const body = buildPolicyBody(policy);
  assert.equal(body.playStoreMode, 'BLACKLIST');
  assert.equal(body.cameraDisabled, true);
  assert.equal(body.installUnknownSourcesAllowed, true);
  assert.deepEqual(body.passwordPolicies, [{ passwordScope: 'SCOPE_PROFILE' }]);
});

test('generateColabStep1Code injects the project ID correctly', () => {
  const code = generateColabStep1Code('android-project-2026');
  assert.ok(code.includes('PROJECT_ID = "android-project-2026"'));
  assert.ok(code.includes('androidmanagement.signupUrls().create'));
});

test('generateColabStep2Code injects enterpriseToken and policy', () => {
  const code = generateColabStep2Code('android-project-2026', 'TOKEN_ABC_XYZ', mockDefaultPolicy);
  assert.ok(code.includes('ENTERPRISING_TOKEN = "TOKEN_ABC_XYZ"'));
  assert.ok(code.includes('"playStoreMode": "BLACKLIST"'));
  assert.ok(code.includes('enrollmentTokens().create'));
});

test('generateAndroidDpcQrPayload produces compliant Android Enterprise DPC bundle', () => {
  const bundle = generateAndroidDpcQrPayload('ENROLLMENT_TOKEN_SECRET_999');
  assert.equal(
    bundle['android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME'],
    'com.google.android.apps.work.clouddpc/.receivers.CloudDeviceAdminReceiver'
  );
  assert.equal(
    bundle['android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE']['com.google.android.apps.work.clouddpc.EXTRA_ENROLLMENT_TOKEN'],
    'ENROLLMENT_TOKEN_SECRET_999'
  );
  assert.equal(bundle['android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED'], true);
});
