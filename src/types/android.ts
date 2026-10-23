export type PlayStoreMode = 'BLACKLIST' | 'WHITELIST';
export type PasswordScope = 'SCOPE_PROFILE' | 'SCOPE_DEVICE' | 'SCOPE_UNSPECIFIED';

export interface AndroidPolicy {
  playStoreMode: PlayStoreMode;
  passwordScope: PasswordScope;
  cameraDisabled: boolean;
  screenCaptureDisabled: boolean;
  crossProfileCopyPasteAllowed: boolean;
  installUnknownSourcesAllowed: boolean;
  modifyAccountsDisabled: boolean;
  factoryResetDisabled: boolean;
  autoTimeRequired: boolean;
}

export interface EnterpriseState {
  projectId: string;
  enterpriseToken: string;
  signupUrlName: string;
  enterpriseName: string;
  enrollmentToken: string;
  policy: AndroidPolicy;
}

export interface AndroidDpcQrBundle {
  'android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME': string;
  'android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM'?: string;
  'android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION'?: string;
  'android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE': {
    'com.google.android.apps.work.clouddpc.EXTRA_ENROLLMENT_TOKEN': string;
  };
  'android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED'?: boolean;
}
